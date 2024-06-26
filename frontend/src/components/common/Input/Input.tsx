import { ComponentPropsWithRef, ForwardedRef, ReactElement, forwardRef } from 'react';

import {
  inputStyle,
  inputWrapperStyle,
  sizeStyle,
  variantStyle,
} from '@/components/common/Input/Input.style';
import Label from '@/components/common/Label/Label';
import SupportingText from '@/components/common/SupportingText/SupportingText';

import { SizeType } from '@/type/size';

export interface InputProps extends Omit<ComponentPropsWithRef<'input'>, 'size'> {
  label?: string;
  variant?: 'default' | 'text';
  size?: Extract<SizeType, 'large' | 'medium' | 'small'>;
  isError?: boolean;
  supportingText?: string;
  icon?: ReactElement;
}

const Input = (
  {
    label,
    variant = 'default',
    size = 'medium',
    isError = false,
    supportingText,
    icon,
    ...attributes
  }: InputProps,
  ref: ForwardedRef<HTMLInputElement>,
) => {
  return (
    <div>
      <Label id={label}>{label}</Label>
      <div css={[sizeStyle[size], variantStyle[variant], inputWrapperStyle(isError)]}>
        {icon}
        <input ref={ref} css={[sizeStyle[size], inputStyle]} {...attributes} />
      </div>
      {supportingText && <SupportingText isError={isError}>{supportingText}</SupportingText>}
    </div>
  );
};

export default forwardRef(Input);
